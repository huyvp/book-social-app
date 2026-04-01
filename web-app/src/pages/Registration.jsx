import {
  Box,
  Button,
  Card,
  CardContent,
  TextField,
  Typography
} from '@mui/material';
import SnackbarUI from '../components/ui/Snackbar';
import { useEffect, useState } from 'react';
import httpClient from '../configurations/httpClient';
import { API } from '../configurations/configuration';

export default function Registration() {
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState('');
  const [snackSeverity, setSnackSeverity] = useState('info');

  const handleCloseSnackBar = () => {
    setSnackBarOpen(false);
  };

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [firstname, setFirstname] = useState('');
  const [lastname, setLastname] = useState('');
  const [email, setEmail] = useState('');

  const handleOnSubmit = async (event) => {
    event.preventDefault();
    const payload = {
      username: username,
      password: password,
      firstName: firstname,
      lastName: lastname,
      email: email
    };

    const data = httpClient.post(API.SIGN_UP, payload);

    if (data.code === 1000) {
      setSnackSeverity('success');
      setSnackBarMessage('Registration completed successfully!');
      setSnackBarOpen(true);
    } else {
      setSnackSeverity('error');
      setSnackBarMessage(data.message);
      setSnackBarOpen(true);
      return;
    }
  };

  useEffect(() => {
    window.document.title = 'Sign Up';
  }, []);

  return (
    <>
      <SnackbarUI
        open={snackBarOpen}
        message={snackBarMessage}
        onClose={handleCloseSnackBar}
        severity={snackSeverity}
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
            minWidth: 400,
            maxWidth: 500,
            boxShadow: 3,
            borderRadius: 3,
            padding: 4
          }}
        >
          <CardContent>
            <Typography variant='h5' component='h1' gutterBottom>
              Welcome, Let's create an account
            </Typography>
            <Box
              component='form'
              display='flex'
              flexDirection='column'
              alignItems='center'
              justifyContent='center'
              width='100%'
              onSubmit={handleOnSubmit}
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
              <TextField
                label='Email'
                type='email'
                variant='outlined'
                fullWidth
                margin='normal'
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
              <TextField
                label='Firstname'
                variant='outlined'
                fullWidth
                margin='normal'
                value={firstname}
                onChange={(e) => setFirstname(e.target.value)}
              />
              <TextField
                label='Lastname'
                variant='outlined'
                fullWidth
                margin='normal'
                value={lastname}
                onChange={(e) => setLastname(e.target.value)}
              />
              <Button
                type='submit'
                variant='contained'
                color='primary'
                size='large'
                onClick={handleOnSubmit}
                fullWidth
                sx={{ mt: '15px', mb: '15px' }}
              >
                Sign Up
              </Button>
            </Box>
          </CardContent>
        </Card>
      </Box>
    </>
  );
}
