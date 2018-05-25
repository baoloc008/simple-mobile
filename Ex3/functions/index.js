const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('job').onWrite((event) => {
  const payload = {
    notification: {
      title: 'Công việc mới',
      body: 'Vừa có công việc mới được đăng tải',
      icon: 'photoURL',
      sound: 'default',
      clickAction: 'fcm.ACTION.HELLO',
      // badge: '1'
    },
    data: {
      extra: 'extra_data',
    },
  };
  const options = {
    collapseKey: 'demo',
    contentAvailable: true,
    priority: 'high',
    timeToLive: 60 * 60 * 24,
  };
  const topic = 'all';
  return admin.messaging().sendToTopic(topic, payload, options)
    .then(response => console.log('Successfully sent message:', response));
});