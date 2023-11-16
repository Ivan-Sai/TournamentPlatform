import {Routes} from "@angular/router";
import {TournamentPageComponent} from "./tournament_page/tournament-page.component";
import {TournamentsAllComponent} from "./tournaments-all/tournaments-all.component";


export const TOURNAMENT_ROUTES: Routes = [

  {
    path: ':id',
    component: TournamentPageComponent
  },
  {
    path: '',
    component: TournamentsAllComponent
  }
];
