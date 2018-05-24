const admin = require('../firebase');

admin.pushNotifyWithToken(
  'cQ8HYtKq5kQ:APA91bHA_xzvum4BndcP_TOUDHXXaySsN8CFSkwPAoau7RUPe2lvLNUWLJ1Fq2IAnCrD2Qu3gZVA4K2KTw8NrUYj3WpUxpoP_UEvpSm8BREI0MQqq2E5NZMR0tn_5GfnoNPuYA2hRBV_',
  { message: 'hello' },
);
