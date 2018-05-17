const getExample = (req, res) => {
  console.log(`Request: ${req.url}`);
  res.json({
    message: 'api/example is running',
  });
};

module.exports = {
  getExample,
};
