import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { map } from 'rxjs/operators';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { DataFile } from './data-file.model';
import { createRequestOption } from '../../shared';

export type DataFileResponseType = HttpResponse<DataFile>;
// File Save support
import { saveAs } from 'file-saver';

@Injectable()
export class DataFileService {
    private resourceImportUrl = SERVER_API_URL + 'api/whitelist-import';
    private resourceExportUrl = SERVER_API_URL + 'api/whitelist-export';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) {}

    create(dataFile: DataFile): Observable<DataFileResponseType> {
        const copy = this.convert(dataFile);
        return this.http
            .post<DataFile>(this.resourceImportUrl, copy, { observe: 'response' })
            .pipe(map((res: DataFileResponseType) => this.convertResponse(res)));
    }

    private convertResponse(res: DataFileResponseType): DataFileResponseType {
        const body: DataFile = this.convertItemFromServer(res.body);
        return res.clone({ body });
    }

    /**
     * Convert a returned JSON object to DataFile.
     */
    private convertItemFromServer(dataFile: DataFile): DataFile {
        const copy: DataFile = Object.assign({}, dataFile);
        copy.createdAt = this.dateUtils.convertDateTimeFromServer(dataFile.createdAt);
        return copy;
    }

    /**
     * Convert a DataFile to a JSON which can be sent to the server.
     */
    private convert(dataFile: DataFile): DataFile {
        const copy: DataFile = Object.assign({}, dataFile);
        copy.createdAt = this.dateUtils.toDate(dataFile.createdAt);
        return copy;
    }

    /**
     * Export data into file
     */
    exportData() {
        this.http
            .get(this.resourceExportUrl, { responseType: 'text' })
            .subscribe(res =>
                saveAs(new Blob([res], { type: 'text;charset=utf-8' }), 'whitelist.' + new Date().toISOString().substr(0, 10) + '.url')
            );
    }

    saveData() {
        return this.http.post(this.resourceExportUrl, { observe: 'response' });
    }

    // Send a PUT request to import endpoint to reload data from static file.
    reloadData() {
        return this.http
            .put<DataFile>(this.resourceImportUrl, null, { observe: 'response' })
            .pipe(map((res: DataFileResponseType) => this.convertResponse(res)));
    }
}
