import {
  Box,
  Button,
  Card,
  CardContent,
  Divider,
  TextField,
  Typography
} from '@mui/material';

import GoogleIcon from '@mui/icons-material/Google';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { isAuthenticated, logIn } from '../services/authenticationService';
import SnackbarUI from '../components/ui/Snackbar';

export default function Login() {
  const navigate = useNavigate();

  const handleCloseSnackBar = () => {
    setSnackBarOpen(false);
  };

  const handleClick = () => {
    alert(
      'Please refer to Oauth2 series for this implemetation guidelines. https://www.youtube.com/playlist?list=PL2xsxmVse9IbweCh6QKqZhousfEWabSeq'
    );
  };

  useEffect(() => {
    if (isAuthenticated()) {
      navigate('/');
    }
  }, [navigate]);

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      await logIn(username, password);
      navigate('/');
    } catch (error) {
      const errorResponse = error.response.data;
      setSnackBarMessage(errorResponse.message);
      setSnackBarOpen(true);
    }
  };

  return (
    <>
      <SnackbarUI
        open={snackBarOpen}
        message={snackBarMessage}
        onClose={handleCloseSnackBar}
        severity='success'
      />
      <Box
        display='flex'
        flexDirection='column'
        alignItems='center'
        justifyContent='center'
        height='100vh'
        bgcolor={'#f0f2f5'}
      >
        <Card
          sx={{
            minWidth: 500,
            maxWidth: 400,
            boxShadow: 3,
            borderRadius: 3,
            padding: 4
          }}
        >
          <CardContent>
            <Typography variant='h5' component='h1' gutterBottom>
              Welcome to _sudo.dan
            </Typography>
            <Box
              component='form'
              display='flex'
              flexDirection='column'
              alignItems='center'
              justifyContent='center'
              width='100%'
              onSubmit={handleSubmit}
            >
              <TextField
                label='Username'
                variant='outlined'
                fullWidth
                margin='normal'
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
              <TextField
                label='Password'
                type='password'
                variant='outlined'
                fullWidth
                margin='normal'
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              <Button
                type='submit'
                variant='contained'
                color='primary'
                size='large'
                onClick={handleSubmit}
                fullWidth
                sx={{
                  mt: '15px',
                  mb: '25px'
                }}
              >
                Login
              </Button>
              <Divider></Divider>
            </Box>

            <Box display='flex' flexDirection='column' width='100%' gap='25px'>
              <Button
                type='button'
                variant='contained'
                color='secondary'
                size='large'
                onClick={handleClick}
                fullWidth
                sx={{ gap: '10px' }}
              >
                <GoogleIcon />
                Continue with Google
              </Button>
              <Button
                type='submit'
                variant='contained'
                color='success'
                size='large'
              >
                Create an account
              </Button>
            </Box>
          </CardContent>
        </Card>
      </Box>
    </>
  );
}
