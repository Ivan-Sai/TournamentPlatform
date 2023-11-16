import {Routes} from "@angular/router";
import {TeamPageComponent} from "./team-page/team-page.component";
import {TeamCreationComponent} from "./team-creation/team-creation.component";
import {TeamInviteLinkComponent} from "./team-invite-link/team-invite-link.component";


export const TEAM_ROUTES: Routes = [
  {
    path: 'create',
    component: TeamCreationComponent
  },
  {
    path: ':id/:UUID/inviteLink',
    component: TeamInviteLinkComponent
  },
  {
    path: ':id',
    component: TeamPageComponent
  },
];
