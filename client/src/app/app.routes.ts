import {Routes} from "@angular/router";
import {HomeComponent} from "./pages/open/home/home.component";
import {SignupComponent} from "./pages/open/auth/signup/signup.component";
import {SigninComponent} from "./pages/open/auth/signin/signin.component";
import {AuthGuard} from "./AuthGuard";

export const APP_ROUTES: Routes = [
  {
    path: "",
    pathMatch: "full",
    component: HomeComponent
  },
  {
    path: "signup",
    pathMatch: "full",
    component: SignupComponent
  },
  {
    path: "signin",
    pathMatch: "full",
    component: SigninComponent
  },
  {
    path: "admin",
    pathMatch: "prefix",
    loadChildren: () => import('./pages/admin/admin-routes').then(r => r.ADMIN_ROUTES),
    canActivate: [AuthGuard],
  },
  {
    path: "tournaments",
    pathMatch: "prefix",
    loadChildren: () => import('./pages/open/tournament/tournament.routes').then(r => r.TOURNAMENT_ROUTES)
  },
  {
    path: 'error',
    pathMatch: "prefix",
    loadChildren: () => import('./pages/open/error/error.routes').then(r => r.ERROR_ROUTES)
  },
  {
    path: 'users',
    pathMatch: "prefix",
    loadChildren: () => import('./pages/open/user-profile/user-routes').then(r=> r.USER_ROUTES)
  },
  {
    path: 'teams',
    pathMatch: "prefix",
    loadChildren: () => import('./pages/open/team/team-routes').then(r=> r.TEAM_ROUTES)
  },
]
