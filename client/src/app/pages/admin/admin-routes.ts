import {Routes} from "@angular/router";
import {TournamentCreationComponent} from "./tournament-creation/tournament-creation.component";

export const ADMIN_ROUTES: Routes = [

  {
    path: 'tournament',
    component: TournamentCreationComponent
  }
];
