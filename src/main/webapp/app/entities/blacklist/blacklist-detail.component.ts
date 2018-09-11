import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBlacklist } from 'app/shared/model/blacklist.model';

@Component({
    selector: 'jhi-blacklist-detail',
    templateUrl: './blacklist-detail.component.html'
})
export class BlacklistDetailComponent implements OnInit {
    blacklist: IBlacklist;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ blacklist }) => {
            this.blacklist = blacklist;
        });
    }

    previousState() {
        window.history.back();
    }
}
