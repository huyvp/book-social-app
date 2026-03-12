import React from 'react';
import { Alert, Snackbar } from '@mui/material';

export default function SnackbarUI({
  open,
  message,
  severity = 'success',
  autoHideDuration = 3000,
  onClose,
  anchorOrigin = { vertical: 'top', horizontal: 'right' },
  sx
}) {
  return (
    <Snackbar
      open={open}
      autoHideDuration={autoHideDuration}
      anchorOrigin={anchorOrigin}
      onClose={onClose}
      sx={sx}
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
