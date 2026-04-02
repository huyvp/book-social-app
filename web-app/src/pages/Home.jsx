import AddIcon from '@mui/icons-material/Add';
import {
  Box,
  Button,
  CircularProgress,
  Dialog,
  DialogContent,
  DialogTitle,
  Fab,
  TextField,
  Tooltip,
  Typography
} from '@mui/material';
import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import Post from '../components/Post';
import SnackbarUI from '../components/ui/Snackbar';
import { isAuthenticated, logOut } from '../services/authenticationService';
import { createPost, getMyPosts } from '../services/postService';
import Scene from './Scene';

const ACCENT = '#1e293b';

export default function HomePage() {
  const navigate = useNavigate();

  const [posts, setPosts] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [hasMore, setHasMore] = useState(false);
  const [newPostContent, setNewPostContent] = useState('');
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false);
  const [isPosting, setIsPosting] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });

  const observer = useRef();
  const lastPostRef = useRef();

  const showSnackbar = (message, severity = 'success') =>
    setSnackbar({ open: true, message, severity });

  const handleCloseSnackbar = () =>
    setSnackbar((prev) => ({ ...prev, open: false }));

  // ─── Auth guard & load posts ────────────────────────────────────────────────
  useEffect(() => {
    if (!isAuthenticated()) {
      navigate('/login');
    } else {
      loadPosts(page);
    }
  }, [navigate, page]);

  const loadPosts = (pageNum) => {
    setLoading(true);
    getMyPosts(pageNum)
      .then((response) => {
        setTotalPages(response.data.result.totalPage);
        setPosts((prevPosts) => {
          if (pageNum === 1) return response.data.result.data;
          return [...prevPosts, ...response.data.result.data];
        });
        setHasMore(response.data.result.data.length > 0);
      })
      .catch((error) => {
        if (error?.response?.status === 401) {
          logOut();
          navigate('/login');
        }
      })
      .finally(() => setLoading(false));
  };

  // ─── Infinite scroll (restoring exact original observer pattern) ────────────
  useEffect(() => {
    if (!hasMore) return;

    if (observer.current) observer.current.disconnect();

    observer.current = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting) {
        if (page < totalPages) {
          setPage((prevPage) => prevPage + 1);
        }
      }
    });

    if (lastPostRef.current) {
      observer.current.observe(lastPostRef.current);
    }

    // Unset hasMore just like original code
    setHasMore(false);
  }, [hasMore, page, totalPages]);


  // ─── Create post ─────────────────────────────────────────────────────────────
  const handleCreatePost = async () => {
    if (!newPostContent.trim()) return;
    setIsPosting(true);
    try {
      const response = await createPost(newPostContent);
      setPosts((prevPosts) => [response.data.result, ...prevPosts]);
      setNewPostContent('');
      setIsCreateDialogOpen(false);
      showSnackbar('Post created successfully!');
    } catch {
      showSnackbar('Failed to create post. Please try again.', 'error');
    } finally {
      setIsPosting(false);
    }
  };

  return (
    <Scene>
      <SnackbarUI
        open={snackbar.open}
        onClose={handleCloseSnackbar}
        message={snackbar.message}
        severity={snackbar.severity}
      />

      {/* Feed */}
      <Box
        sx={{
          maxWidth: 620,
          width: '100%',
          mx: 'auto',
          px: { xs: 1.5, sm: 2 },
          py: 3,
          display: 'flex',
          flexDirection: 'column',
          gap: 2
        }}
      >
        {/* Section title */}
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <Typography
            sx={{ fontWeight: 700, fontSize: '1.1rem', color: '#111827' }}
          >
            Your Posts
          </Typography>
          <Typography sx={{ fontSize: '0.8rem', color: '#9ca3af' }}>
            {posts.length} post{posts.length !== 1 ? 's' : ''}
          </Typography>
        </Box>

        {/* Posts */}
        {posts.map((post, index) => {
          const isLast = posts.length === index + 1;

          if (isLast) {
            return (
              <Box
                key={post.id}
                ref={lastPostRef}
                sx={postBoxStyles}
              >
                <Post post={post} />
              </Box>
            );
          } else {
            return (
              <Box
                key={post.id}
                sx={postBoxStyles}
              >
                <Post post={post} />
              </Box>
            );
          }
        })}

        {/* Loading */}
        {loading && (
          <Box sx={{ display: 'flex', justifyContent: 'center', py: 3 }}>
            <CircularProgress size={26} sx={{ color: ACCENT }} />
          </Box>
        )}

        {/* Empty state */}
        {!loading && posts.length === 0 && (
          <Box
            sx={{
              bgcolor: '#fff',
              borderRadius: '16px',
              border: '1px solid rgba(0,0,0,0.06)',
              p: 5,
              textAlign: 'center'
            }}
          >
            <Typography fontSize='2.5rem' mb={1}>📝</Typography>
            <Typography fontWeight={600} color='#374151' mb={0.5}>
              No posts yet
            </Typography>
            <Typography fontSize='0.875rem' color='text.secondary'>
              Click the + button to share something!
            </Typography>
          </Box>
        )}
      </Box>

      {/* FAB */}
      <Tooltip title='New post' placement='left'>
        <Fab
          onClick={() => setIsCreateDialogOpen(true)}
          sx={{
            position: 'fixed',
            bottom: 28,
            right: 28,
            bgcolor: ACCENT,
            color: '#fff',
            boxShadow: '0 6px 20px rgba(30,41,59,0.45)',
            '&:hover': { bgcolor: '#0f172a' }
          }}
        >
          <AddIcon />
        </Fab>
      </Tooltip>

      {/* Create post dialog */}
      <Dialog
        open={isCreateDialogOpen}
        onClose={() => setIsCreateDialogOpen(false)}
        maxWidth='sm'
        fullWidth
        PaperProps={{
          sx: {
            borderRadius: '20px',
            p: 1
          }
        }}
      >
        <DialogTitle sx={{ fontWeight: 700, pb: 1 }}>Create Post</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            fullWidth
            multiline
            rows={4}
            placeholder="What's on your mind?"
            value={newPostContent}
            onChange={(e) => setNewPostContent(e.target.value)}
            variant='outlined'
            sx={{
              mt: 1,
              '& .MuiOutlinedInput-root': {
                borderRadius: '12px',
                '&.Mui-focused fieldset': { borderColor: ACCENT }
              }
            }}
          />
          <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 1, mt: 2 }}>
            <Button
              variant='outlined'
              onClick={() => setIsCreateDialogOpen(false)}
              sx={{
                borderRadius: '10px',
                textTransform: 'none',
                borderColor: '#e5e7eb',
                color: '#374151'
              }}
            >
              Cancel
            </Button>
            <Button
              variant='contained'
              onClick={handleCreatePost}
              disabled={!newPostContent.trim() || isPosting}
              sx={{
                borderRadius: '10px',
                textTransform: 'none',
                bgcolor: ACCENT,
                boxShadow: 'none',
                '&:hover': { bgcolor: '#0f172a', boxShadow: 'none' },
                '&:disabled': { bgcolor: '#e5e7eb', color: '#9ca3af' }
              }}
            >
              {isPosting ? <CircularProgress size={18} sx={{ color: '#fff' }} /> : 'Post'}
            </Button>
          </Box>
        </DialogContent>
      </Dialog>
    </Scene>
  );
}

const postBoxStyles = {
  bgcolor: '#fff',
  borderRadius: '16px',
  border: '1px solid rgba(0,0,0,0.06)',
  boxShadow: '0 2px 10px rgba(0,0,0,0.05)',
  overflow: 'hidden',
  p: { xs: 2.5, sm: 3 }
};
