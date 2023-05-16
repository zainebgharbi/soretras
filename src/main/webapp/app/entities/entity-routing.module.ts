import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'contest',
        data: { pageTitle: 'Contests' },
        loadChildren: () => import('./contest/contest.module').then(m => m.ContestModule),
      },
      {
        path: 'contestform',
        data: { pageTitle: 'Contestforms' },
        loadChildren: () => import('./contestform/contestform.module').then(m => m.ContestformModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
