import { Moment } from 'moment';
import { IArtifact } from 'app/shared/model/artifact.model';
import { ICourse } from 'app/shared/model/course.model';

export interface IMilestone {
  id?: number;
  description?: string;
  deadline?: Moment;
  artifacts?: IArtifact[];
  course?: ICourse;
}

export class Milestone implements IMilestone {
  constructor(
    public id?: number,
    public description?: string,
    public deadline?: Moment,
    public artifacts?: IArtifact[],
    public course?: ICourse
  ) {}
}
