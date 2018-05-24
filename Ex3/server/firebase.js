const admin = require('firebase-admin');

const serviceAccount = require('./private/jobfinder-adminsdk.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://jobfinder-a5df.firebaseio.com',
});

function pushNotifyWithToken(registrationToken, title, body) {
  const data = {
    android: { notification: { title, body } },
    token: registrationToken,
  };
  admin.messaging().send(data)
    .then(res => console.log('Send message successfully: ', res))
    .catch(err => console.log('Error while send message: ', err));
}

module.exports = {
  pushNotifyWithToken,
};
