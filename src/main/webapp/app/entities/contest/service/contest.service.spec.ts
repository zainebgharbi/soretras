import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IContest } from '../contest.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../contest.test-samples';

import { ContestService, RestContest } from './contest.service';

const requireRestSample: RestContest = {
  ...sampleWithRequiredData,
  begindate: sampleWithRequiredData.begindate?.format(DATE_FORMAT),
  enddate: sampleWithRequiredData.enddate?.format(DATE_FORMAT),
};

describe('Contest Service', () => {
  let service: ContestService;
  let httpMock: HttpTestingController;
  let expectedResult: IContest | IContest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContestService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Contest', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const contest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Contest', () => {
      const contest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Contest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Contest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Contest', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addContestToCollectionIfMissing', () => {
      it('should add a Contest to an empty array', () => {
        const contest: IContest = sampleWithRequiredData;
        expectedResult = service.addContestToCollectionIfMissing([], contest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contest);
      });

      it('should not add a Contest to an array that contains it', () => {
        const contest: IContest = sampleWithRequiredData;
        const contestCollection: IContest[] = [
          {
            ...contest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContestToCollectionIfMissing(contestCollection, contest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Contest to an array that doesn't contain it", () => {
        const contest: IContest = sampleWithRequiredData;
        const contestCollection: IContest[] = [sampleWithPartialData];
        expectedResult = service.addContestToCollectionIfMissing(contestCollection, contest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contest);
      });

      it('should add only unique Contest to an array', () => {
        const contestArray: IContest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contestCollection: IContest[] = [sampleWithRequiredData];
        expectedResult = service.addContestToCollectionIfMissing(contestCollection, ...contestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contest: IContest = sampleWithRequiredData;
        const contest2: IContest = sampleWithPartialData;
        expectedResult = service.addContestToCollectionIfMissing([], contest, contest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contest);
        expect(expectedResult).toContain(contest2);
      });

      it('should accept null and undefined values', () => {
        const contest: IContest = sampleWithRequiredData;
        expectedResult = service.addContestToCollectionIfMissing([], null, contest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contest);
      });

      it('should return initial array if no Contest is added', () => {
        const contestCollection: IContest[] = [sampleWithRequiredData];
        expectedResult = service.addContestToCollectionIfMissing(contestCollection, undefined, null);
        expect(expectedResult).toEqual(contestCollection);
      });
    });

    describe('compareContest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareContest(entity1, entity2);
        const compareResult2 = service.compareContest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareContest(entity1, entity2);
        const compareResult2 = service.compareContest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareContest(entity1, entity2);
        const compareResult2 = service.compareContest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
