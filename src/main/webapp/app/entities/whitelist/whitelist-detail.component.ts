import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWhitelist } from 'app/shared/model/whitelist.model';

@Component({
    selector: 'jhi-whitelist-detail',
    templateUrl: './whitelist-detail.component.html'
})
export class WhitelistDetailComponent implements OnInit {
    whitelist: IWhitelist;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ whitelist }) => {
            this.whitelist = whitelist;
        });
    }

    previousState() {
        window.history.back();
    }
}
