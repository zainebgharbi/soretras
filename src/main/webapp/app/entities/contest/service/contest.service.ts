import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContest, NewContest } from '../contest.model';

export type PartialUpdateContest = Partial<IContest> & Pick<IContest, 'id'>;

type RestOf<T extends IContest | NewContest> = Omit<T, 'begindate' | 'enddate'> & {
  begindate?: string | null;
  enddate?: string | null;
};

export type RestContest = RestOf<IContest>;

export type NewRestContest = RestOf<NewContest>;

export type PartialUpdateRestContest = RestOf<PartialUpdateContest>;

export type EntityResponseType = HttpResponse<IContest>;
export type EntityArrayResponseType = HttpResponse<IContest[]>;

@Injectable({ providedIn: 'root' })
export class ContestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contest: NewContest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contest);
    return this.http
      .post<RestContest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(contest: IContest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contest);
    return this.http
      .put<RestContest>(`${this.resourceUrl}/${this.getContestIdentifier(contest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(contest: PartialUpdateContest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contest);
    return this.http
      .patch<RestContest>(`${this.resourceUrl}/${this.getContestIdentifier(contest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestContest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestContest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContestIdentifier(contest: Pick<IContest, 'id'>): number {
    return contest.id;
  }

  compareContest(o1: Pick<IContest, 'id'> | null, o2: Pick<IContest, 'id'> | null): boolean {
    return o1 && o2 ? this.getContestIdentifier(o1) === this.getContestIdentifier(o2) : o1 === o2;
  }

  addContestToCollectionIfMissing<Type extends Pick<IContest, 'id'>>(
    contestCollection: Type[],
    ...contestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contests: Type[] = contestsToCheck.filter(isPresent);
    if (contests.length > 0) {
      const contestCollectionIdentifiers = contestCollection.map(contestItem => this.getContestIdentifier(contestItem)!);
      const contestsToAdd = contests.filter(contestItem => {
        const contestIdentifier = this.getContestIdentifier(contestItem);
        if (contestCollectionIdentifiers.includes(contestIdentifier)) {
          return false;
        }
        contestCollectionIdentifiers.push(contestIdentifier);
        return true;
      });
      return [...contestsToAdd, ...contestCollection];
    }
    return contestCollection;
  }

  protected convertDateFromClient<T extends IContest | NewContest | PartialUpdateContest>(contest: T): RestOf<T> {
    return {
      ...contest,
      begindate: contest.begindate?.format(DATE_FORMAT) ?? null,
      enddate: contest.enddate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restContest: RestContest): IContest {
    return {
      ...restContest,
      begindate: restContest.begindate ? dayjs(restContest.begindate) : undefined,
      enddate: restContest.enddate ? dayjs(restContest.enddate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestContest>): HttpResponse<IContest> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestContest[]>): HttpResponse<IContest[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
