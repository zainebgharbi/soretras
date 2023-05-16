import { Routes } from '@angular/router';

import { ErrorComponent } from './error.component';

export const errorRoute: Routes = [
  {
    path: 'error',
    component: ErrorComponent,
    data: {
      pageTitle: 'صفحة الاخطاء',
    },
  },
  {
    path: 'accessdenied',
    component: ErrorComponent,
    data: {
      pageTitle: 'صفحة الاخطاء',
      errorMessage: 'غير مصرح لك للوصول إلى صفحة.',
    },
  },
  {
    path: '404',
    component: ErrorComponent,
    data: {
      pageTitle: 'صفحة الاخطاء',
      errorMessage: 'هذه الصفحة غير موجودة.',
    },
  },
  {
    path: '**',
    redirectTo: '/404',
  },
];
