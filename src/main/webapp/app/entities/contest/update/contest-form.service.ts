import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IContest, NewContest } from '../contest.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContest for edit and NewContestFormGroupInput for create.
 */
type ContestFormGroupInput = IContest | PartialWithRequiredKeyOf<NewContest>;

type ContestFormDefaults = Pick<NewContest, 'id'>;

type ContestFormGroupContent = {
  id: FormControl<IContest['id'] | NewContest['id']>;
  name: FormControl<IContest['name']>;
  description: FormControl<IContest['description']>;
  begindate: FormControl<IContest['begindate']>;
  enddate: FormControl<IContest['enddate']>;
  level: FormControl<IContest['level']>;
};

export type ContestFormGroup = FormGroup<ContestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContestFormService {
  createContestFormGroup(contest: ContestFormGroupInput = { id: null }): ContestFormGroup {
    const contestRawValue = {
      ...this.getFormDefaults(),
      ...contest,
    };
    return new FormGroup<ContestFormGroupContent>({
      id: new FormControl(
        { value: contestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(contestRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(contestRawValue.description),
      begindate: new FormControl(contestRawValue.begindate),
      enddate: new FormControl(contestRawValue.enddate),
      level: new FormControl(contestRawValue.level),
    });
  }

  getContest(form: ContestFormGroup): IContest | NewContest {
    return form.getRawValue() as IContest | NewContest;
  }

  resetForm(form: ContestFormGroup, contest: ContestFormGroupInput): void {
    const contestRawValue = { ...this.getFormDefaults(), ...contest };
    form.reset(
      {
        ...contestRawValue,
        id: { value: contestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ContestFormDefaults {
    return {
      id: null,
    };
  }
}
