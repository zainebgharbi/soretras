import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContest } from '../contest.model';
import { ContestService } from '../service/contest.service';

@Injectable({ providedIn: 'root' })
export class ContestRoutingResolveService implements Resolve<IContest | null> {
  constructor(protected service: ContestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContest | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contest: HttpResponse<IContest>) => {
          if (contest.body) {
            return of(contest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
