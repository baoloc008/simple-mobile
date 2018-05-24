const { admin } = require('../firebase');
const locationData = require('../data/location');
const jobData = require('../data/jobs');
const jobTypeData = require('../data/job-type');

const db = admin.database();
const ref = db.ref('job-finder-database');

const locationRef = ref.child('location');
locationRef.set(locationData);

const jobDataRef = ref.child('job');
jobDataRef.set(jobData);

const jobTypeRef = ref.child('job-type');
jobTypeRef.set(jobTypeData);
