import dayjs from 'dayjs/esm';

import { elevel } from 'app/entities/enumerations/elevel.model';

import { IContest, NewContest } from './contest.model';

export const sampleWithRequiredData: IContest = {
  id: 54628,
  name: 'generate',
};

export const sampleWithPartialData: IContest = {
  id: 86348,
  name: 'Refined architecture Checking',
  description: 'fuchsia',
  level: elevel['BAC'],
};

export const sampleWithFullData: IContest = {
  id: 47333,
  name: 'Licensed SCSI',
  description: 'THX indexing',
  begindate: dayjs('2023-05-16'),
  enddate: dayjs('2023-05-16'),
  level: elevel['LICENSE'],
};

export const sampleWithNewData: NewContest = {
  name: 'Automated',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
