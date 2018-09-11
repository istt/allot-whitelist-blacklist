import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBlacklist } from 'app/shared/model/blacklist.model';

@Component({
    selector: 'jhi-blacklist-detail',
    templateUrl: './blacklist-detail.component.html'
})
export class BlacklistDetailComponent implements OnInit {
    blacklist: IBlacklist;

    constructor(private route: ActivatedRoute) {}

    ngOnInit() {
        this.route.data.subscribe(({ blacklist }) => {
            this.blacklist = blacklist.body ? blacklist.body : blacklist;
        });
    }

    previousState() {
        window.history.back();
    }
}
