import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Blacklist } from 'app/shared/model/blacklist.model';
import { BlacklistService } from './blacklist.service';
import { BlacklistComponent } from './blacklist.component';
import { BlacklistDetailComponent } from './blacklist-detail.component';
import { BlacklistUpdateComponent } from './blacklist-update.component';
import { BlacklistDeletePopupComponent } from './blacklist-delete-dialog.component';
import { IBlacklist } from 'app/shared/model/blacklist.model';

@Injectable({ providedIn: 'root' })
export class BlacklistResolve implements Resolve<IBlacklist> {
    constructor(private service: BlacklistService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((blacklist: HttpResponse<Blacklist>) => blacklist.body));
        }
        return of(new Blacklist());
    }
}

export const blacklistRoute: Routes = [
    {
        path: 'blacklist',
        component: BlacklistComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
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
    }
];
