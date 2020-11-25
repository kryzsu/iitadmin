import { IMilestone } from 'app/shared/model/milestone.model';

export interface ICourse {
  id?: number;
  name?: string;
  milestones?: IMilestone[];
}

export class Course implements ICourse {
  constructor(public id?: number, public name?: string, public milestones?: IMilestone[]) {}
}
