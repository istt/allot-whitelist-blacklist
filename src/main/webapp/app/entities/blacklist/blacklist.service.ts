import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBlacklist } from 'app/shared/model/blacklist.model';

type EntityResponseType = HttpResponse<IBlacklist>;
type EntityArrayResponseType = HttpResponse<IBlacklist[]>;

@Injectable()
export class BlacklistService {
    private resourceUrl = SERVER_API_URL + 'api/blacklists';

    constructor(private http: HttpClient) {}

    create(blacklist: IBlacklist): Observable<EntityResponseType> {
        return this.http.post<IBlacklist>(this.resourceUrl, blacklist, { observe: 'response' });
    }

    update(blacklist: IBlacklist): Observable<EntityResponseType> {
        return this.http.put<IBlacklist>(this.resourceUrl, blacklist, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IBlacklist>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBlacklist[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
