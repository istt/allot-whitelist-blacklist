import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IWhitelist } from 'app/shared/model/whitelist.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { WhitelistService } from './whitelist.service';
// Import & Export support
import { DataFileService } from './data-file.service';

@Component({
    selector: 'jhi-whitelist',
    templateUrl: './whitelist.component.html'
})
export class WhitelistComponent implements OnInit, OnDestroy {
    currentAccount: any;
    whitelists: IWhitelist[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    searchModel: IWhitelist = {};

    constructor(
        public whitelistService: WhitelistService,
        public dataFileService: DataFileService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.whitelistService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IWhitelist[]>) => this.paginateWhitelists(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/whitelist'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/whitelist',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInWhitelists();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IWhitelist) {
        return item.id;
    }

    registerChangeInWhitelists() {
        this.eventSubscriber = this.eventManager.subscribe('whitelistListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateWhitelists(data: IWhitelist[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.whitelists = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    // Extra functions
    exportData() {
        this.dataFileService.exportData();
    }

    searchReset() {
        this.searchModel = {};
        this.transition();
    }

    search() {
        this.whitelistService
            .query(
                Object.assign(
                    {
                        page: this.page - 1,
                        size: this.itemsPerPage,
                        sort: this.sort()
                    },
                    this.createSearchQuery()
                )
            )
            .subscribe(
                (res: HttpResponse<IWhitelist[]>) => this.paginateWhitelists(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }
    // TODO: Create the JPA Model filtering engine
    createSearchQuery() {
        const search = {};
        if (this.searchModel.id) {
            search['id.equals'] = this.searchModel.id;
        }
        if (this.searchModel.url) {
            search['url.contains'] = this.searchModel.url;
        }
        return search;
    }
}
