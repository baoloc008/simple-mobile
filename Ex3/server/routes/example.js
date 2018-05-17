const express = require('express');

const router = express.Router();

const exampleControllers = require('../controllers/example');

router.get('/', exampleControllers.getExample);

module.exports = router;
