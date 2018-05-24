const request = require('request');
const cheerio = require('cheerio');
const fs = require('fs');

request('https://careerbuilder.vn/', (error, response, body) => {
  const $ = cheerio.load(body);

  const tobJobs = $('li.top-job')
    .map((i, e) => {
      const a = $(e).find('.jobtitle a');
      const url = a.attr('href');
      const jobTitle = a.text();
      const company = $(e)
        .find('.jobcompany a')
        .text();
      const salaryLocation = $(e).find('.salary-location p');
      const salaryNode = salaryLocation[0].childNodes;
      const salary = salaryNode.length > 1 ? salaryNode[1].data : salaryNode[0].data;
      const location = salaryLocation[1].childNodes[1].data;
      const thumbnail = $(e)
        .find('.ftable img')
        .attr('src');
      return {
        url,
        jobTitle,
        company,
        salary,
        location,
        thumbnail,
      };
    })
    .get();
  console.log(tobJobs);
  fs.writeFileSync('../data/jobs.json', JSON.stringify(tobJobs)).then(res => console.log(res));
});
