import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from 'app/core';
import { Blacklist } from 'app/shared/model/blacklist.model';
import { BlacklistService } from './blacklist.service';
import { BlacklistComponent } from './blacklist.component';
import { BlacklistDetailComponent } from './blacklist-detail.component';
import { BlacklistUpdateComponent } from './blacklist-update.component';
import { BlacklistDeletePopupComponent } from './blacklist-delete-dialog.component';
// Extra components
import { DataFilePopupComponent } from './data-file-dialog.component';

@Injectable()
export class BlacklistResolvePagingParams implements Resolve<any> {
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
export class BlacklistResolve implements Resolve<any> {
    constructor(private service: BlacklistService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id);
        }
        return new Blacklist();
    }
}

export const blacklistRoute: Routes = [
    {
        path: 'blacklist',
        component: BlacklistComponent,
        resolve: {
            pagingParams: BlacklistResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appApp.blacklist.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'blacklist/:id/view',
        component: BlacklistDetailComponent,
        resolve: {
            blacklist: BlacklistResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appApp.blacklist.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'blacklist/new',
        component: BlacklistUpdateComponent,
        resolve: {
            blacklist: BlacklistResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appApp.blacklist.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'blacklist/:id/edit',
        component: BlacklistUpdateComponent,
        resolve: {
            blacklist: BlacklistResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appApp.blacklist.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const blacklistPopupRoute: Routes = [
    {
        path: 'blacklist/:id/delete',
        component: BlacklistDeletePopupComponent,
        resolve: {
            blacklist: BlacklistResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'appApp.blacklist.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }, // Import & Export support
    {
        path: 'blacklist-import',
        component: DataFilePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gatewayApp.dnd.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
