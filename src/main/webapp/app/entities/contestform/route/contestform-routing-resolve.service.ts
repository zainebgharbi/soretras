import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContestform } from '../contestform.model';
import { ContestformService } from '../service/contestform.service';

@Injectable({ providedIn: 'root' })
export class ContestformRoutingResolveService implements Resolve<IContestform | null> {
  constructor(protected service: ContestformService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContestform | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contestform: HttpResponse<IContestform>) => {
          if (contestform.body) {
            return of(contestform.body);
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
