import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IArtifact } from 'app/shared/model/artifact.model';

type EntityResponseType = HttpResponse<IArtifact>;
type EntityArrayResponseType = HttpResponse<IArtifact[]>;

@Injectable({ providedIn: 'root' })
export class ArtifactService {
  public resourceUrl = SERVER_API_URL + 'api/artifacts';

  constructor(protected http: HttpClient) {}

  create(artifact: IArtifact): Observable<EntityResponseType> {
    return this.http.post<IArtifact>(this.resourceUrl, artifact, { observe: 'response' });
  }

  update(artifact: IArtifact): Observable<EntityResponseType> {
    return this.http.put<IArtifact>(this.resourceUrl, artifact, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IArtifact>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IArtifact[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
