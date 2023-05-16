import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IContestform, NewContestform } from '../contestform.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContestform for edit and NewContestformFormGroupInput for create.
 */
type ContestformFormGroupInput = IContestform | PartialWithRequiredKeyOf<NewContestform>;

type ContestformFormDefaults = Pick<NewContestform, 'id'>;

type ContestformFormGroupContent = {
  id: FormControl<IContestform['id'] | NewContestform['id']>;
  firstname: FormControl<IContestform['firstname']>;
  lastname: FormControl<IContestform['lastname']>;
  birthdate: FormControl<IContestform['birthdate']>;
  contest: FormControl<IContestform['contest']>;
};

export type ContestformFormGroup = FormGroup<ContestformFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContestformFormService {
  createContestformFormGroup(contestform: ContestformFormGroupInput = { id: null }): ContestformFormGroup {
    const contestformRawValue = {
      ...this.getFormDefaults(),
      ...contestform,
    };
    return new FormGroup<ContestformFormGroupContent>({
      id: new FormControl(
        { value: contestformRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstname: new FormControl(contestformRawValue.firstname, {
        validators: [Validators.required],
      }),
      lastname: new FormControl(contestformRawValue.lastname, {
        validators: [Validators.required],
      }),
      birthdate: new FormControl(contestformRawValue.birthdate),
      contest: new FormControl(contestformRawValue.contest, {
        validators: [Validators.required],
      }),
    });
  }

  getContestform(form: ContestformFormGroup): IContestform | NewContestform {
    return form.getRawValue() as IContestform | NewContestform;
  }

  resetForm(form: ContestformFormGroup, contestform: ContestformFormGroupInput): void {
    const contestformRawValue = { ...this.getFormDefaults(), ...contestform };
    form.reset(
      {
        ...contestformRawValue,
        id: { value: contestformRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ContestformFormDefaults {
    return {
      id: null,
    };
  }
}
