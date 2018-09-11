import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IWhitelist } from 'app/shared/model/whitelist.model';
import { WhitelistService } from './whitelist.service';

@Component({
    selector: 'jhi-whitelist-update',
    templateUrl: './whitelist-update.component.html'
})
export class WhitelistUpdateComponent implements OnInit {
    private _whitelist: IWhitelist;
    isSaving: boolean;

    constructor(private whitelistService: WhitelistService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ whitelist }) => {
            this.whitelist = whitelist;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.whitelist.id !== undefined) {
            this.subscribeToSaveResponse(this.whitelistService.update(this.whitelist));
        } else {
            this.subscribeToSaveResponse(this.whitelistService.create(this.whitelist));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWhitelist>>) {
        result.subscribe((res: HttpResponse<IWhitelist>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get whitelist() {
        return this._whitelist;
    }

    set whitelist(whitelist: IWhitelist) {
        this._whitelist = whitelist;
    }
}
