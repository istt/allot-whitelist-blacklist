import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IWhitelist, Whitelist } from 'app/shared/model/whitelist.model';
import { WhitelistService } from './whitelist.service';
import { WhitelistComponent } from './whitelist.component';
import { WhitelistDetailComponent } from './whitelist-detail.component';
import { WhitelistUpdateComponent } from './whitelist-update.component';
import { WhitelistDeletePopupComponent } from './whitelist-delete-dialog.component';
// Extra components
import { DataFilePopupComponent } from './data-file-dialog.component';

@Injectable({ providedIn: 'root' })
export class WhitelistResolve implements Resolve<IWhitelist> {
    constructor(private service: WhitelistService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((whitelist: HttpResponse<Whitelist>) => whitelist.body));
        }
        return of(new Whitelist());
    }
}

export const whitelistRoute: Routes = [
    {
        path: 'whitelist',
        component: WhitelistComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
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
    }, // Import & Export support
    {
        path: 'whitelist-import',
        component: DataFilePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gatewayApp.dnd.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
