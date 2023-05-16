import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContestformComponent } from '../list/contestform.component';
import { ContestformDetailComponent } from '../detail/contestform-detail.component';
import { ContestformUpdateComponent } from '../update/contestform-update.component';
import { ContestformRoutingResolveService } from './contestform-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const contestformRoute: Routes = [
  {
    path: '',
    component: ContestformComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContestformDetailComponent,
    resolve: {
      contestform: ContestformRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContestformUpdateComponent,
    resolve: {
      contestform: ContestformRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContestformUpdateComponent,
    resolve: {
      contestform: ContestformRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contestformRoute)],
  exports: [RouterModule],
})
export class ContestformRoutingModule {}
