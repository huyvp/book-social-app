import React from 'react';
import { Alert, Snackbar } from '@mui/material';

export default function SnackbarUI({
  open,
  message,
  severity = 'success',
  autoHideDuration = 3000,
  onClose
}) {
  return (
    <Snackbar
      open={open}
      autoHideDuration={autoHideDuration}
      anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
      onClose={onClose}
    >
      <Alert
        onClose={onClose}
        severity={severity}
        variant='filled'
        sx={{ width: '100%' }}
      >
        {message}
      </Alert>
    </Snackbar>
  );
}
