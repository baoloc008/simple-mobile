const admin = require('firebase-admin');

const serviceAccount = require('./private/jobfinder-a5dfb-firebase-adminsdk-1fstg-43382a90b2.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://jobfinder-a5dfb.firebaseio.com',
});

function pushNotifyWithToken(registrationToken, data) {
  try {
    const message = {
      data: {
        title: 'Job Finder',
        message: data.message,
      },
      token: registrationToken,
    };
    admin.messaging().send(message).then(response => {
      console.log('Successfully sent message:', response);
    })
  } catch (error) {
    console.log('Error sending message:', error);
  }
}
// export function pushNotifyWithTopic(topicName, data) {
//   try {
//     const message = {
//       data: {
//         title: 'Job Finder',
//         message: data.message,
//       },
//       topic: topicName,
//     };
//     const response = await admin.messaging().send(message);
//     console.log('Successfully sent message:', response);
//   } catch (error) {
//     console.log('Error sending message:', error);
//   }
// }

module.exports = {
  pushNotifyWithToken
};
