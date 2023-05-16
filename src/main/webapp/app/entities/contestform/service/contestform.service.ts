import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContestform, NewContestform } from '../contestform.model';

export type PartialUpdateContestform = Partial<IContestform> & Pick<IContestform, 'id'>;

type RestOf<T extends IContestform | NewContestform> = Omit<T, 'birthdate'> & {
  birthdate?: string | null;
};

export type RestContestform = RestOf<IContestform>;

export type NewRestContestform = RestOf<NewContestform>;

export type PartialUpdateRestContestform = RestOf<PartialUpdateContestform>;

export type EntityResponseType = HttpResponse<IContestform>;
export type EntityArrayResponseType = HttpResponse<IContestform[]>;

@Injectable({ providedIn: 'root' })
export class ContestformService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contestforms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contestform: NewContestform): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contestform);
    return this.http
      .post<RestContestform>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(contestform: IContestform): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contestform);
    return this.http
      .put<RestContestform>(`${this.resourceUrl}/${this.getContestformIdentifier(contestform)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(contestform: PartialUpdateContestform): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contestform);
    return this.http
      .patch<RestContestform>(`${this.resourceUrl}/${this.getContestformIdentifier(contestform)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestContestform>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestContestform[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContestformIdentifier(contestform: Pick<IContestform, 'id'>): number {
    return contestform.id;
  }

  compareContestform(o1: Pick<IContestform, 'id'> | null, o2: Pick<IContestform, 'id'> | null): boolean {
    return o1 && o2 ? this.getContestformIdentifier(o1) === this.getContestformIdentifier(o2) : o1 === o2;
  }

  addContestformToCollectionIfMissing<Type extends Pick<IContestform, 'id'>>(
    contestformCollection: Type[],
    ...contestformsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contestforms: Type[] = contestformsToCheck.filter(isPresent);
    if (contestforms.length > 0) {
      const contestformCollectionIdentifiers = contestformCollection.map(
        contestformItem => this.getContestformIdentifier(contestformItem)!
      );
      const contestformsToAdd = contestforms.filter(contestformItem => {
        const contestformIdentifier = this.getContestformIdentifier(contestformItem);
        if (contestformCollectionIdentifiers.includes(contestformIdentifier)) {
          return false;
        }
        contestformCollectionIdentifiers.push(contestformIdentifier);
        return true;
      });
      return [...contestformsToAdd, ...contestformCollection];
    }
    return contestformCollection;
  }

  protected convertDateFromClient<T extends IContestform | NewContestform | PartialUpdateContestform>(contestform: T): RestOf<T> {
    return {
      ...contestform,
      birthdate: contestform.birthdate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restContestform: RestContestform): IContestform {
    return {
      ...restContestform,
      birthdate: restContestform.birthdate ? dayjs(restContestform.birthdate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestContestform>): HttpResponse<IContestform> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestContestform[]>): HttpResponse<IContestform[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
