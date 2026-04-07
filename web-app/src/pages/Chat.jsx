import AddIcon from '@mui/icons-material/Add';
import RefreshIcon from '@mui/icons-material/Refresh';
import SendIcon from '@mui/icons-material/Send';
import {
  Alert,
  Avatar,
  Badge,
  Box,
  CircularProgress,
  Divider,
  IconButton,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  Stack,
  TextField,
  Tooltip,
  Typography
} from '@mui/material';
import { useCallback, useEffect, useRef, useState } from 'react';

import NewChatPopover from '../components/NewChatPopover';
import {
  createConversation,
  createMessage,
  getMessages,
  getMyConversations
} from '../services/chatService';
import { getMyInfo } from '../services/userService';
import Scene from './Scene';
import { io } from 'socket.io-client';

const ACCENT = '#1e293b'; // Slate 800

export default function ChatPage() {
  const [message, setMessage] = useState('');
  const [newChatAnchorEl, setNewChatAnchorEl] = useState(null);
  const [conversations, setConversations] = useState([]);
  const [isLoadingConversations, setIsLoadingConversations] = useState(false);
  const [conversationsError, setConversationsError] = useState(null);
  const [selectedConversation, setSelectedConversation] = useState(null);
  const [messagesMap, setMessagesMap] = useState({});
  const [showConversationList, setShowConversationList] = useState(true);
  const [myUserId, setMyUserId] = useState(null);

  const messageContainerRef = useRef(null);

  const scrollToBottom = useCallback(() => {
    if (!messageContainerRef.current) return;
    const el = messageContainerRef.current;
    el.scrollTop = el.scrollHeight;
    setTimeout(() => {
      el.scrollTop = el.scrollHeight;
    }, 150);
  }, []);

  const fetchConversations = async () => {
    setIsLoadingConversations(true);
    setConversationsError(null);
    try {
      const response = await getMyConversations();
      setConversations(response?.data?.result || []);
    } catch {
      setConversationsError('Failed to load conversations. Please try again.');
    } finally {
      setIsLoadingConversations(false);
    }
  };

  useEffect(() => {
    fetchConversations();
    getMyInfo()
      .then((res) => {
        const user = res?.data?.result;
        if (user) setMyUserId(user.userId || user.id);
      })
      .catch(() => { });
  }, []);

  useEffect(() => {
    if (conversations.length > 0 && !selectedConversation) {
      setSelectedConversation(conversations[0]);
    }
  }, [conversations, selectedConversation]);

  useEffect(() => {
    if (!selectedConversation?.id) return;

    const fetchMessages = async (conversationId) => {
      try {
        if (!messagesMap[conversationId]) {
          const response = await getMessages(conversationId);
          if (response?.data?.result) {
            const sorted = [...response.data.result].sort(
              (a, b) => new Date(a.createdDate) - new Date(b.createdDate)
            );
            setMessagesMap((prev) => ({ ...prev, [conversationId]: sorted }));
          }
        }
        setConversations((prev) =>
          prev.map((c) => (c.id === conversationId ? { ...c, unread: 0 } : c))
        );
      } catch {
        /* silent */
      }
    };

    fetchMessages(selectedConversation.id);
  }, [selectedConversation, messagesMap]);

  const currentMessages = selectedConversation
    ? (messagesMap[selectedConversation.id] || []).map((m) => {
      if (m.pending || m.me !== undefined) return m;
      // Determine if message is mine
      const senderId = m.sender?.userId || m.sender?.id || m.senderId;
      return { ...m, me: senderId === myUserId };
    })
    : [];

  useEffect(() => {
    scrollToBottom();
  }, [currentMessages, scrollToBottom]);

  useEffect(() => {
    scrollToBottom();
  }, [selectedConversation, scrollToBottom]);

  useEffect(() => {
    console.log('Initializing socket  ...');
    const socket = new io('http://localhost:8099');

    socket.on('connect', () => {
      console.log('socket connected');
    });

    socket.on('disconnect', () => {
      console.log('socket disconnected');
    });

    socket.on('message', (msg) => {
      console.log('message', msg);
    });

    return () => {
      socket.disconnect();
    };
  }, []);

  const handleSelectConversation = (conversation) => {
    setSelectedConversation(conversation);
    setShowConversationList(false);
  };

  const handleSelectNewChatUser = async (user) => {
    const response = await createConversation({
      type: 'DIRECT',
      participantIds: [user.userId]
    });
    const newConv = response?.data?.result;
    const existing = conversations.find((c) => c.id === newConv.id);
    if (existing) {
      setSelectedConversation(existing);
    } else {
      setConversations((prev) => [newConv, ...prev]);
      setSelectedConversation(newConv);
    }
    setNewChatAnchorEl(null);
    setShowConversationList(false);
  };

  const handleSendMessage = async () => {
    if (!message.trim() || !selectedConversation) return;

    const tempId = `temp-${Date.now()}`;
    const optimistic = {
      id: tempId,
      message: message,
      createdDate: new Date().toISOString(),
      me: true,
      pending: true
    };

    setMessagesMap((prev) => ({
      ...prev,
      [selectedConversation.id]: [
        ...(prev[selectedConversation.id] || []),
        optimistic
      ]
    }));
    setConversations((prev) =>
      prev.map((conversation) =>
        conversation.id === selectedConversation.id
          ? {
            ...conversation,
            lastMessage: message,
            lastTimestamp: new Date().toLocaleString()
          }
          : conversation
      )
    );
    setMessage('');

    try {
      const response = await createMessage({
        conversationId: selectedConversation.id,
        message
      });
      if (response?.data?.result) {
        setMessagesMap((prev) => {
          const updated = prev[selectedConversation.id].filter(
            (msg) => msg.id !== tempId
          );
          return {
            ...prev,
            [selectedConversation.id]: [
              ...updated,
              { ...response.data.result, me: true }
            ].sort((a, b) => new Date(a.createdDate) - new Date(b.createdDate))
          };
        });
      }
    } catch {
      setMessagesMap((prev) => ({
        ...prev,
        [selectedConversation.id]: prev[selectedConversation.id].map((m) =>
          m.id === tempId ? { ...m, failed: true, pending: false } : m
        )
      }));
    }
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    return new Date(dateStr).toLocaleString('vi-VN', {
      day: 'numeric',
      month: 'numeric',
      year: 'numeric'
    });
  };

  const formatTime = (dateStr) => {
    if (!dateStr) return '';
    return new Date(dateStr).toLocaleTimeString('vi-VN', {
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  return (
    <Scene>
      <Box
        sx={{
          display: 'flex',
          height: 'calc(100vh - 64px)',
          overflow: 'hidden',
          backgroundColor: '#f0f2f5'
        }}
      >
        {/* ── Conversation List ── */}
        <Box
          sx={{
            width: { xs: '100%', sm: 300, md: 320 },
            flexShrink: 0,
            display: {
              xs: showConversationList ? 'flex' : 'none',
              sm: 'flex'
            },
            flexDirection: 'column',
            backgroundColor: '#fff',
            borderRight: '1px solid rgba(0,0,0,0.07)',
            height: '100%'
          }}
        >
          <Box
            sx={{
              px: 2,
              height: 64,
              minHeight: 64,
              boxSizing: 'border-box',
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              borderBottom: '1px solid rgba(0,0,0,0.07)'
            }}
          >
            <Typography
              sx={{ fontWeight: 700, fontSize: '1.05rem', color: '#111827' }}
            >
              Messages
            </Typography>
            <Tooltip title='New conversation'>
              <IconButton
                size='small'
                onClick={(e) => setNewChatAnchorEl(e.currentTarget)}
                sx={{
                  backgroundColor: ACCENT,
                  color: '#fff',
                  width: 32,
                  height: 32,
                  '&:hover': { backgroundColor: '#0f172a' }
                }}
              >
                <AddIcon fontSize='small' />
              </IconButton>
            </Tooltip>
            <NewChatPopover
              anchorEl={newChatAnchorEl}
              open={Boolean(newChatAnchorEl)}
              onClose={() => setNewChatAnchorEl(null)}
              onSelectUser={handleSelectNewChatUser}
            />
          </Box>

          {/* List body */}
          <Box sx={{ flexGrow: 1, overflowY: 'auto' }}>
            {isLoadingConversations && (
              <Box sx={{ display: 'flex', justifyContent: 'center', p: 3 }}>
                <CircularProgress size={26} sx={{ color: ACCENT }} />
              </Box>
            )}
            {conversationsError && (
              <Box sx={{ p: 2 }}>
                <Alert
                  severity='error'
                  action={
                    <IconButton
                      size='small'
                      color='inherit'
                      onClick={fetchConversations}
                    >
                      <RefreshIcon fontSize='small' />
                    </IconButton>
                  }
                >
                  {conversationsError}
                </Alert>
              </Box>
            )}
            {!isLoadingConversations &&
              !conversationsError &&
              conversations.length === 0 && (
                <Box sx={{ p: 3, textAlign: 'center' }}>
                  <Typography color='text.secondary' fontSize='0.875rem'>
                    No conversations yet.
                    <br />
                    Start a new chat!
                  </Typography>
                </Box>
              )}
            {!isLoadingConversations && conversations.length > 0 && (
              <List disablePadding>
                {conversations.map((conv) => {
                  const isSelected = selectedConversation?.id === conv.id;
                  return (
                    <Box key={conv.id}>
                      <ListItem
                        alignItems='flex-start'
                        onClick={() => handleSelectConversation(conv)}
                        sx={{
                          cursor: 'pointer',
                          px: 2,
                          py: 1.2,
                          backgroundColor: isSelected
                            ? 'rgba(30,41,59,0.08)'
                            : 'transparent',
                          borderLeft: isSelected
                            ? `3px solid ${ACCENT}`
                            : '3px solid transparent',
                          transition: 'all 0.15s ease',
                          '&:hover': { backgroundColor: 'rgba(0,0,0,0.04)' }
                        }}
                      >
                        <ListItemAvatar>
                          <Badge
                            color='error'
                            badgeContent={conv.unread}
                            invisible={!conv.unread}
                            overlap='circular'
                          >
                            <Avatar
                              src={conv.conversationAvatar || ''}
                              sx={{ width: 42, height: 42 }}
                            />
                          </Badge>
                        </ListItemAvatar>
                        <ListItemText
                          primary={
                            <Stack
                              direction='row'
                              justifyContent='space-between'
                              alignItems='center'
                            >
                              <Typography
                                variant='body2'
                                fontWeight={conv.unread > 0 ? 700 : 500}
                                color='#111827'
                                noWrap
                                sx={{ maxWidth: 140 }}
                              >
                                {conv.conversationName}
                              </Typography>
                              <Typography
                                variant='caption'
                                color='text.secondary'
                                sx={{ fontSize: '0.7rem', flexShrink: 0 }}
                              >
                                {formatDate(conv.modifiedDate)}
                              </Typography>
                            </Stack>
                          }
                          secondary={
                            <Typography
                              variant='body2'
                              color='text.secondary'
                              noWrap
                              fontSize='0.8rem'
                            >
                              {conv.lastMessage || 'Start a conversation'}
                            </Typography>
                          }
                        />
                      </ListItem>
                      <Divider variant='inset' component='li' />
                    </Box>
                  );
                })}
              </List>
            )}
          </Box>
        </Box>

        {/* ── Chat Area ── */}
        <Box
          sx={{
            flexGrow: 1,
            display: {
              xs: showConversationList ? 'none' : 'flex',
              sm: 'flex'
            },
            flexDirection: 'column',
            height: '100%',
            backgroundColor: '#fff'
          }}
        >
          {selectedConversation ? (
            <>
              {/* Chat header */}
              <Box
                sx={{
                  px: 2,
                  height: 64, // strictly identical header height
                  minHeight: 64, // avoid vertical shift
                  boxSizing: 'border-box',
                  borderBottom: '1px solid rgba(0,0,0,0.07)',
                  display: 'flex',
                  alignItems: 'center',
                  gap: 1.5
                }}
              >
                {/* Back button — mobile only */}
                <IconButton
                  size='small'
                  onClick={() => setShowConversationList(true)}
                  sx={{ display: { sm: 'none' }, mr: 0.5, color: ACCENT }}
                >
                  ←
                </IconButton>
                <Avatar
                  src={selectedConversation.conversationAvatar}
                  sx={{ width: 38, height: 38 }}
                />
                <Box>
                  <Typography
                    fontWeight={700}
                    fontSize='0.95rem'
                    color='#111827'
                  >
                    {selectedConversation.conversationName}
                  </Typography>
                  <Typography fontSize='0.75rem' color='text.secondary'>
                    Active
                  </Typography>
                </Box>
              </Box>

              {/* Messages */}
              <Box
                ref={messageContainerRef}
                sx={{
                  flexGrow: 1,
                  overflowY: 'auto',
                  p: 2,
                  display: 'flex',
                  flexDirection: 'column',
                  gap: 1,
                  backgroundColor: '#f8f9fb'
                }}
              >
                {currentMessages.map((msg) => (
                  <Box
                    key={msg.id}
                    sx={{
                      display: 'flex',
                      justifyContent: msg.me ? 'flex-end' : 'flex-start',
                      alignItems: 'flex-end',
                      gap: 1
                    }}
                  >
                    {!msg.me && (
                      <Avatar
                        src={msg.sender?.avatar}
                        sx={{ width: 28, height: 28, flexShrink: 0 }}
                      />
                    )}

                    <Box sx={{ maxWidth: { xs: '80%', sm: '65%' } }}>
                      <Box
                        sx={{
                          px: 1.8,
                          py: 1,
                          borderRadius: msg.me
                            ? '18px 18px 4px 18px'
                            : '18px 18px 18px 4px',
                          backgroundColor: msg.me
                            ? msg.failed
                              ? '#fee2e2'
                              : ACCENT
                            : '#f3f4f6',
                          color: msg.me && !msg.failed ? '#fff' : '#111827',
                          opacity: msg.pending ? 0.65 : 1,
                          boxShadow: '0 1px 4px rgba(0,0,0,0.08)'
                        }}
                      >
                        <Typography
                          variant='body2'
                          sx={{ wordBreak: 'break-word', lineHeight: 1.5 }}
                        >
                          {msg.message}
                        </Typography>
                      </Box>

                      <Stack
                        direction='row'
                        spacing={0.5}
                        justifyContent={msg.me ? 'flex-end' : 'flex-start'}
                        sx={{ mt: 0.4, px: 0.5 }}
                      >
                        {msg.failed && (
                          <Typography variant='caption' color='error'>
                            Failed
                          </Typography>
                        )}
                        {msg.pending && (
                          <Typography variant='caption' color='text.secondary'>
                            Sending…
                          </Typography>
                        )}
                        <Typography
                          variant='caption'
                          color='text.secondary'
                          sx={{ fontSize: '0.68rem' }}
                        >
                          {formatTime(msg.createdDate)}
                        </Typography>
                      </Stack>
                    </Box>

                    {msg.me && (
                      <Avatar
                        sx={{
                          width: 28,
                          height: 28,
                          flexShrink: 0,
                          backgroundColor: ACCENT,
                          fontSize: '0.65rem',
                          fontWeight: 700
                        }}
                      >
                        Me
                      </Avatar>
                    )}
                  </Box>
                ))}
              </Box>

              {/* Input */}
              <Box
                component='form'
                onSubmit={(e) => {
                  e.preventDefault();
                  handleSendMessage();
                }}
                sx={{
                  px: 2,
                  py: 1.5,
                  borderTop: '1px solid rgba(0,0,0,0.07)',
                  display: 'flex',
                  gap: 1,
                  alignItems: 'center',
                  backgroundColor: '#fff'
                }}
              >
                <TextField
                  fullWidth
                  placeholder='Type a message…'
                  size='small'
                  value={message}
                  onChange={(e) => setMessage(e.target.value)}
                  onKeyDown={(e) => {
                    if (e.key === 'Enter' && !e.shiftKey) {
                      e.preventDefault();
                      handleSendMessage();
                    }
                  }}
                  sx={{
                    '& .MuiOutlinedInput-root': {
                      borderRadius: '20px',
                      backgroundColor: '#f3f4f6',
                      '& fieldset': { border: 'none' },
                      '&.Mui-focused': { backgroundColor: '#ececf1' }
                    }
                  }}
                />
                <Tooltip title='Send (Enter)'>
                  <span>
                    <IconButton
                      type='submit'
                      disabled={!message.trim()}
                      sx={{
                        backgroundColor: message.trim() ? ACCENT : '#e5e7eb',
                        color: message.trim() ? '#fff' : '#9ca3af',
                        width: 38,
                        height: 38,
                        transition: 'all 0.2s',
                        '&:hover': {
                          backgroundColor: message.trim()
                            ? '#0f172a'
                            : '#e5e7eb'
                        }
                      }}
                    >
                      <SendIcon fontSize='small' />
                    </IconButton>
                  </span>
                </Tooltip>
              </Box>
            </>
          ) : (
            <Box
              sx={{
                flexGrow: 1,
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                gap: 1,
                color: 'text.secondary'
              }}
            >
              <Typography fontSize='2.5rem'>💬</Typography>
              <Typography fontWeight={600} color='#374151'>
                Your messages
              </Typography>
              <Typography fontSize='0.875rem' color='text.secondary'>
                Select a conversation or start a new one
              </Typography>
            </Box>
          )}
        </Box>
      </Box>
    </Scene>
  );
}
