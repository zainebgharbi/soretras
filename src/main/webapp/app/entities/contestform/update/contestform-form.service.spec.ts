import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../contestform.test-samples';

import { ContestformFormService } from './contestform-form.service';

describe('Contestform Form Service', () => {
  let service: ContestformFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContestformFormService);
  });

  describe('Service methods', () => {
    describe('createContestformFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContestformFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstname: expect.any(Object),
            lastname: expect.any(Object),
            birthdate: expect.any(Object),
            contest: expect.any(Object),
          })
        );
      });

      it('passing IContestform should create a new form with FormGroup', () => {
        const formGroup = service.createContestformFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstname: expect.any(Object),
            lastname: expect.any(Object),
            birthdate: expect.any(Object),
            contest: expect.any(Object),
          })
        );
      });
    });

    describe('getContestform', () => {
      it('should return NewContestform for default Contestform initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createContestformFormGroup(sampleWithNewData);

        const contestform = service.getContestform(formGroup) as any;

        expect(contestform).toMatchObject(sampleWithNewData);
      });

      it('should return NewContestform for empty Contestform initial value', () => {
        const formGroup = service.createContestformFormGroup();

        const contestform = service.getContestform(formGroup) as any;

        expect(contestform).toMatchObject({});
      });

      it('should return IContestform', () => {
        const formGroup = service.createContestformFormGroup(sampleWithRequiredData);

        const contestform = service.getContestform(formGroup) as any;

        expect(contestform).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContestform should not enable id FormControl', () => {
        const formGroup = service.createContestformFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContestform should disable id FormControl', () => {
        const formGroup = service.createContestformFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
