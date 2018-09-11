import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { IBlacklist } from 'app/shared/model/blacklist.model';
import { BlacklistService } from './blacklist.service';

@Component({
    selector: 'jhi-blacklist-update',
    templateUrl: './blacklist-update.component.html'
})
export class BlacklistUpdateComponent implements OnInit {
    private _blacklist: IBlacklist;
    isSaving: boolean;

    constructor(private blacklistService: BlacklistService, private route: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.route.data.subscribe(({ blacklist }) => {
            this.blacklist = blacklist.body ? blacklist.body : blacklist;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.blacklist.id !== undefined) {
            this.subscribeToSaveResponse(this.blacklistService.update(this.blacklist));
        } else {
            this.subscribeToSaveResponse(this.blacklistService.create(this.blacklist));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBlacklist>>) {
        result.subscribe((res: HttpResponse<IBlacklist>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get blacklist() {
        return this._blacklist;
    }

    set blacklist(blacklist: IBlacklist) {
        this._blacklist = blacklist;
    }
}
