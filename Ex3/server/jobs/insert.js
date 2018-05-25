const { admin } = require('../firebase');
const locationData = require('../data/location');
const jobData = require('../data/jobs');
const jobTypeData = require('../data/job-type');
const jobDetailData = require('../data/jobDetail.json');

const db = admin.database();
const ref = db.ref();

const locationRef = ref.child('location');
const jobDataRef = ref.child('job');
const jobTypeRef = ref.child('job-type');
const jobDetailRef = ref.child('job-detail');


Promise.all([
  locationRef.set(locationData),
  jobDataRef.set(jobData),
  jobTypeRef.set(jobTypeData),
  jobDetailRef.set(jobDetailData),
])
  .then(() => {
    console.log('Insert database successfully');
    process.exit(0);
  })
  .catch((err) => {
    console.log('Insert database failed with error: ', err);
    process.exit(1);
  });
