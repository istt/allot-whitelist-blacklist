/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs/observable/of';

import { AppTestModule } from '../../../test.module';
import { WhitelistDetailComponent } from 'app/entities/whitelist/whitelist-detail.component';
import { Whitelist } from 'app/shared/model/whitelist.model';

describe('Component Tests', () => {
    describe('Whitelist Management Detail Component', () => {
        let comp: WhitelistDetailComponent;
        let fixture: ComponentFixture<WhitelistDetailComponent>;
        const route = ({ data: of({ whitelist: new Whitelist(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AppTestModule],
                declarations: [WhitelistDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(WhitelistDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WhitelistDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.whitelist).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
