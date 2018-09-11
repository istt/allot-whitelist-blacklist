import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWhitelist } from 'app/shared/model/whitelist.model';

@Component({
    selector: 'jhi-whitelist-detail',
    templateUrl: './whitelist-detail.component.html'
})
export class WhitelistDetailComponent implements OnInit {
    whitelist: IWhitelist;

    constructor(private route: ActivatedRoute) {}

    ngOnInit() {
        this.route.data.subscribe(({ whitelist }) => {
            this.whitelist = whitelist.body ? whitelist.body : whitelist;
        });
    }

    previousState() {
        window.history.back();
    }
}
