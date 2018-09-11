import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from 'app/core';
import { Whitelist } from 'app/shared/model/whitelist.model';
import { WhitelistService } from './whitelist.service';
import { WhitelistComponent } from './whitelist.component';
import { WhitelistDetailComponent } from './whitelist-detail.component';
import { WhitelistUpdateComponent } from './whitelist-update.component';
import { WhitelistDeletePopupComponent } from './whitelist-delete-dialog.component';

@Injectable()
export class WhitelistResolvePagingParams implements Resolve<any> {
    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
        };
    }
}

@Injectable()
export class WhitelistResolve implements Resolve<any> {
    constructor(private service: WhitelistService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id);
        }
        return new Whitelist();
    }
}

export const whitelistRoute: Routes = [
    {
        path: 'whitelist',
        component: WhitelistComponent,
        resolve: {
            pagingParams: WhitelistResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appApp.whitelist.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'whitelist/:id/view',
        component: WhitelistDetailComponent,
        resolve: {
            whitelist: WhitelistResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appApp.whitelist.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'whitelist/new',
        component: WhitelistUpdateComponent,
        resolve: {
            whitelist: WhitelistResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appApp.whitelist.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'whitelist/:id/edit',
        component: WhitelistUpdateComponent,
        resolve: {
            whitelist: WhitelistResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appApp.whitelist.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const whitelistPopupRoute: Routes = [
    {
        path: 'whitelist/:id/delete',
        component: WhitelistDeletePopupComponent,
        resolve: {
            whitelist: WhitelistResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appApp.whitelist.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
