import dayjs from 'dayjs/esm';
import { elevel } from 'app/entities/enumerations/elevel.model';

export interface IContest {
  id: number;
  name?: string | null;
  description?: string | null;
  begindate?: dayjs.Dayjs | null;
  enddate?: dayjs.Dayjs | null;
  level?: elevel | null;
}

export type NewContest = Omit<IContest, 'id'> & { id: null };
