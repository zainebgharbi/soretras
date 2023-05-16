import dayjs from 'dayjs/esm';

import { IContestform, NewContestform } from './contestform.model';

export const sampleWithRequiredData: IContestform = {
  id: 40453,
  firstname: 'Small Steel transparent',
  lastname: 'Sports Electronics synergies',
};

export const sampleWithPartialData: IContestform = {
  id: 86853,
  firstname: 'interface',
  lastname: 'Oregon',
  birthdate: dayjs('2023-05-15'),
};

export const sampleWithFullData: IContestform = {
  id: 69997,
  firstname: 'Movies neural',
  lastname: 'circuit Wall',
  birthdate: dayjs('2023-05-16'),
};

export const sampleWithNewData: NewContestform = {
  firstname: 'web-enabled',
  lastname: 'Washington',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
