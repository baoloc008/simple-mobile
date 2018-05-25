const rp = require('request-promise').defaults({ family: 4 });
const cheerio = require('cheerio');
const fs = require('fs');
const path = require('path');

const filePath = path.join(__dirname, '../data/jobDetail.json');

const jobs = require('../data/jobs.json');

const getData = (html, id) => {
  const $ = cheerio.load(html);
  const result = {
    id,
    jobTitle: 'AP/GL accountant',
    company: 'Công Ty TNHH Phát Triển Phú Mỹ Hưng',
    locations: ['Hồ Chí Minh'],
    experience: '1 - 2 Năm',
    majors: ['Kế toán / Kiểm toán', 'Ngân hàng', 'Tài chính / Đầu tư'],
    position: 'Nhân viên',
    salaray: 'Cạnh tranh',
    expired: '20/06/2018',
    benefits: [
      'Bảo hiểm theo quy định',
      'Du Lịch',
      'Phụ cấp',
      'Đồng phục',
      'Thưởng',
      'Đào tạo',
      'Tăng lương',
      'Chế độ nghỉ phép',
    ],
    description: 'Check daily payment request document.\nMonthly closing and prepare relevant report/statements.\nPreparing document to auditor.\nSupporting manager/head of team for ad-hoc work.\nOthers assigned by head of team\nAble to be on board asap',
    requirements: 'more than 01 year experience\nable to communicate in English\nexperience working in construction industry is preferred',
    otherInfo: 'Bằng cấp: Đại học\nĐộ tuổi: Không giới hạn tuổi\nHình thức: Nhân viên chính thức',
    introduce: 'Công ty TNHH Phát triển Phú Mỹ Hưng\n801 Nguyễn Văn Linh, Phường Tân Phú, Quận 7, HCM\nNgười liên hệ: Ms. Uyên',
  };

  const detailJobNew = $('.DetailJobNew');

  if (detailJobNew.length === 1) {
    result.locations = detailJobNew
      .find('.bgLine1 .fl_left b a')
      .get()
      .map(l => l.children[0].data);
    result.salary = detailJobNew.find('.bgLine1 .fl_right label').text();
  }
  return result;
};

const promises = jobs.map((element) => {
  const { id } = element;
  const option = {
    uri: encodeURI(id),
    transform: body => getData(body, id),
  };

  return rp(option);
});

// promises.forEach(p => p.then(res => jobDetails.push(res)).catch(ex => console.log('error', ex)));

Promise.all(promises).then(res => fs.writeFileSync(filePath, JSON.stringify(res)));
