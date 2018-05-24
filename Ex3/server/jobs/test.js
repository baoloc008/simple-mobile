const admin = require('../firebase');

const token = 'curhgsRa17c:APA91bFbMLlmAwSiFJJSxL2m4pmOGiq5AY9fgktGwsMjStxc_ZF06epsKs_izktyGheIlOe_mepx-4rH2nEbLIJqhIrjgoCu6a1yFHlw728CGR2HxNszEbOnjQmCTM7Zd-lszC3b9JeP';

admin.pushNotifyWithToken(token, 'Thông báo', 'Chào bạn, chúng tôi đến từ Job Finder');

