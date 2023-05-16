import dayjs from 'dayjs/esm';
import { IContest } from 'app/entities/contest/contest.model';

export interface IContestform {
  id: number;
  firstname?: string | null;
  lastname?: string | null;
  birthdate?: dayjs.Dayjs | null;
  contest?: Pick<IContest, 'id'> | null;
}

export type NewContestform = Omit<IContestform, 'id'> & { id: null };
