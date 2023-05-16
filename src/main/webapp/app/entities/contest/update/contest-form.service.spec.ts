import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../contest.test-samples';

import { ContestFormService } from './contest-form.service';

describe('Contest Form Service', () => {
  let service: ContestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContestFormService);
  });

  describe('Service methods', () => {
    describe('createContestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            begindate: expect.any(Object),
            enddate: expect.any(Object),
            level: expect.any(Object),
          })
        );
      });

      it('passing IContest should create a new form with FormGroup', () => {
        const formGroup = service.createContestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            begindate: expect.any(Object),
            enddate: expect.any(Object),
            level: expect.any(Object),
          })
        );
      });
    });

    describe('getContest', () => {
      it('should return NewContest for default Contest initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createContestFormGroup(sampleWithNewData);

        const contest = service.getContest(formGroup) as any;

        expect(contest).toMatchObject(sampleWithNewData);
      });

      it('should return NewContest for empty Contest initial value', () => {
        const formGroup = service.createContestFormGroup();

        const contest = service.getContest(formGroup) as any;

        expect(contest).toMatchObject({});
      });

      it('should return IContest', () => {
        const formGroup = service.createContestFormGroup(sampleWithRequiredData);

        const contest = service.getContest(formGroup) as any;

        expect(contest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContest should not enable id FormControl', () => {
        const formGroup = service.createContestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContest should disable id FormControl', () => {
        const formGroup = service.createContestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
