import { IMilestone } from 'app/shared/model/milestone.model';

export interface IArtifact {
  id?: number;
  name?: string;
  dataContentType?: string;
  data?: any;
  milestone?: IMilestone;
}

export class Artifact implements IArtifact {
  constructor(
    public id?: number,
    public name?: string,
    public dataContentType?: string,
    public data?: any,
    public milestone?: IMilestone
  ) {}
}
