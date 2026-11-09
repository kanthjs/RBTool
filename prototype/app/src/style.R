# Bootstrap 5 through bslib. bs_theme(version = 5) with no Sass variables changed is served precompiled, so the
# browser does not compile Sass when the app opens; the colours of this app are set in app_css instead.
brand <- c(main = "#A61C1C", dark = "#7A1212", light = "#F8F4F4")
# Google Sans (from Google Fonts, Latin and Thai); Chulabhorn Likit, bundled in www/fonts, is the fallback when it cannot load
google_sans_link <- tags$link(rel = "stylesheet", href = "https://fonts.googleapis.com/css2?family=Google+Sans:wght@400;500;600;700&display=swap")
# sprintf() refuses format strings over 8192 characters, so the brand colours are filled in by css_fill()
css_fill <- function(x, ...) {
  a <- c(...)
  for (i in seq_along(a)) {
    x <- gsub(sprintf("%%%d$s", i), a[[i]], x, fixed = TRUE)
  }
  gsub("%%", "%", x, fixed = TRUE)
}
app_css <- css_fill(
  "@font-face { font-family: 'Chulabhorn Likit'; src: url('fonts/ChulabhornLikitText-Regular.ttf') format('truetype'); font-weight: normal; }
    @font-face { font-family: 'Chulabhorn Likit'; src: url('fonts/ChulabhornLikitText-Bold.ttf') format('truetype'); font-weight: bold; }
    :root { --bs-body-font-family: 'Google Sans', 'Chulabhorn Likit', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
            --bs-body-font-size: 15px;
            --bs-primary: %1$s; --bs-primary-rgb: 166, 28, 28;
            --bs-link-color: %1$s; --bs-link-color-rgb: 166, 28, 28;
            --bs-link-hover-color: %2$s; --bs-link-hover-color-rgb: 122, 18, 18;
            --bs-body-bg: #fbfafa; }
    .btn, input, select, textarea, .selectize-input, .selectize-dropdown { font-family: var(--bs-body-font-family); }
    .form-control, .form-select, .selectize-input { border-color: #e2dbdb; border-radius: 6px; }
    .form-control:focus, .form-select:focus, .selectize-input.focus { border-color: %1$s; box-shadow: 0 0 0 3px rgba(166, 28, 28, .14); }
    .btn-primary { --bs-btn-bg: %1$s; --bs-btn-border-color: %1$s; --bs-btn-hover-bg: %2$s; --bs-btn-hover-border-color: %2$s;
                   --bs-btn-active-bg: %2$s; --bs-btn-active-border-color: %2$s; --bs-btn-disabled-bg: %1$s; --bs-btn-disabled-border-color: %1$s; }
    .btn { margin: 0 4px 8px 0; border-radius: 6px; } h4 { margin-top: 18px; }
    .btn-default:not(.btn-primary) { padding: 4px 12px; font-size: 13px; background: #fff; border: 1px solid #e0d8d8; color: #3b2d2e; }
    .btn-default:not(.btn-primary):hover { background: #f5f0f0; border-color: %1$s; color: %2$s; }
    .navbar-brand { font-weight: bold; letter-spacing: .3px; } .navbar-brand .badge { font-weight: normal; margin-left: 6px; vertical-align: middle; }
    .navbar .nav-link { padding-left: 14px !important; padding-right: 14px !important; }
    
    /* Clean sidebar without nested boxes */
    .well { background: #fff; border: 1px solid #ebe5e5; border-radius: 12px; box-shadow: none; padding: 18px 18px 0; }
    .well:has(.side-step) { background: #fff; border-color: #ebe5e5; }
    .side-step { background: transparent; border: none; border-bottom: 1px solid #f4f0f0; border-radius: 0; padding: 0 0 16px 0; margin-bottom: 16px; box-shadow: none; }
    .side-step:last-of-type { border-bottom: none; margin-bottom: 8px; }
    .side-step-title { display: flex; align-items: center; gap: 8px; font-weight: 600; font-size: 14.5px; color: #2e2021; margin: 0 0 10px; padding: 0; border-bottom: none; }
    .side-step-title .num { flex: none; width: 22px; height: 22px; border-radius: 6px; background: #f2eaea; color: %1$s; display: flex; align-items: center; justify-content: center; font-size: 12px; font-weight: 700; }
    .side-step .shiny-input-container label, .side-step .control-label { font-size: 13.5px; font-weight: 500; color: #443637; margin-bottom: 4px; }
    
    /* Subtle minimal advanced options */
    details.advanced { background: transparent; border: none; border-radius: 0; padding: 4px 0; margin: 0 0 14px; }
    details.advanced > summary { cursor: pointer; color: #6a5859; font-weight: 500; font-size: 13.5px; padding: 4px 0; user-select: none; }
    details.advanced > summary:hover { color: %1$s; }
    .advanced-body { padding: 8px 0 0; }
    
    /* Sticky bottom run bar with clean frosted glass effect */
    .run-bar { position: sticky; bottom: 0; z-index: 5; background: rgba(255, 255, 255, .95); backdrop-filter: blur(8px); -webkit-backdrop-filter: blur(8px); margin: 8px -18px 0; padding: 12px 18px; border-top: 1px solid #f2eded; border-radius: 0 0 12px 12px; }
    .run-bar .btn { margin: 0; padding: 9px; font-weight: 600; font-size: 14.5px; }

    /* Minimal tab navigation */
    .nav-tabs { border-bottom: 1px solid #ebe5e5; margin-bottom: 16px; }
    .nav-tabs .nav-link { border: none; border-bottom: 2px solid transparent; color: #6d5b5c; font-weight: 500; padding: 8px 16px; background: transparent; margin-bottom: -1px; }
    .nav-tabs .nav-link:hover { color: %1$s; border-color: transparent; }
    .nav-tabs .nav-link.active { color: %1$s; font-weight: 600; background: transparent; border: none; border-bottom: 2px solid %1$s; }

    .shiny-input-container input[type=radio]:checked, .shiny-input-container input[type=checkbox]:checked { background-color: %1$s; border-color: %1$s; }
    details.help-box { margin: 4px 0 12px; font-size: 13px; }
    details.help-box summary { cursor: pointer; color: #6e5c5d; font-weight: 500; }
    details.help-box summary:hover { color: %1$s; }
    .radio-group-title { font-size: 11.5px; font-weight: 700; text-transform: uppercase; letter-spacing: .4px; color: %1$s; margin: 10px 0 4px; padding-bottom: 2px; border-bottom: 1px solid #f0e6e6; }
    .shiny-options-group > .radio-group-title:first-child { margin-top: 0; }
    .layout-help { background: #faf6f6; border: 1px solid #efe6e6; border-radius: 8px; padding: 8px 10px; }
    .layout-table { font-size: 11.5px; border-collapse: collapse; margin: 2px 0 8px; width: 100%%; background: #fff; }
    .layout-table th { background: #f2eaea; color: %2$s; font-weight: 600; }
    .layout-table th, .layout-table td { border: 1px solid #e6dcdc; padding: 2px 5px; text-align: center; white-space: nowrap; }
    .layout-rules { padding-left: 18px; margin-bottom: 6px; font-size: 12.5px; } .layout-rules li { margin-bottom: 3px; }
    .help-body { padding: 6px 0 6px 10px; border-left: 2px solid #e8e0e0; margin-top: 4px; color: #555; }
    details.plot-labels { margin-bottom: 12px; } details.plot-labels summary { cursor: pointer; color: #6e5c5d; margin-bottom: 8px; font-size: 13.5px; }
    
    /* Clean Hero & Home */
    .hero { background: linear-gradient(135deg, %1$s, %2$s); color: #fff; border-radius: 12px; padding: 30px 32px; margin: 8px 0 24px; }
    .hero h2 { font-weight: bold; margin: 0 0 6px; } .hero p { margin: 0; opacity: .92; font-size: 15.5px; }
    .home-card { border: 1px solid #ebe5e5; border-radius: 10px; box-shadow: none; transition: border-color .15s, box-shadow .15s; }
    .home-card:hover { border-color: #d6c4c4; box-shadow: 0 4px 16px rgba(0,0,0,.04); }
    .home-card .icon { width: 46px; height: 46px; border-radius: 10px; background: #f5eeee; color: %1$s; display: flex; align-items: center; justify-content: center; font-size: 20px; margin-bottom: 10px; }
    .home-card h4 { margin-top: 0; font-size: 1.15rem; font-weight: 600; color: #261a1b; } .home-card .card-body { gap: .4rem; } .home-card p { color: #555; font-size: 14px; }
    .steps { display: flex; gap: 16px; flex-wrap: wrap; margin: 8px 0 20px; } .step { flex: 1 1 200px; display: flex; gap: 10px; align-items: flex-start; }
    .step .num { flex: none; width: 28px; height: 28px; border-radius: 6px; background: #f2eaea; color: %1$s; display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 13px; }
    
    /* Data tab: every row in a scroll box */
    .data-scroll { max-height: 600px; overflow: auto; border: 1px solid #ede8e8; border-radius: 6px; }
    .data-scroll .table-responsive { overflow: visible; }
    .data-scroll thead th { position: sticky; top: 0; z-index: 2; --bs-table-bg: #fff; --bs-table-bg-state: #fff; background-color: #fff !important; box-shadow: inset 0 -1px 0 #ede8e8; }
    /* what-you-get box under the choice of analysis */
    .get-box { background: #fbf6f6; border-left: 3px solid #A61C1C; border-radius: 6px; padding: 8px 10px; margin-top: 4px; font-size: 12.5px; line-height: 1.5; }
    .get-box p { margin: 0 0 4px; }
    .get-box ul { margin: 0 0 6px; padding-left: 18px; }
    .get-box li { margin-bottom: 2px; }
    .get-box .get-q { margin: 6px 0 0; color: #A61C1C; font-weight: 500; }

    /* Result cards */
    .result-card { border-radius: 8px; border: 1px solid #ebe5e5; background: #fff; margin-bottom: 16px; box-shadow: none; }
    .result-card .card-header { background: #fff; font-weight: 600; color: #2a1c1d; border-bottom: 1px solid #f4f0f0; padding: 9px 16px; }
    .result-card .card-header .btn { margin: 0; background: #fdfcfc; border: 1px solid #e4dcdc; color: #5d4b4c; font-size: 12px; padding: 2px 10px; border-radius: 5px; }
    .result-card .card-header .btn:hover { background: #f5eded; color: %1$s; border-color: %1$s; }
    .result-card h6 { font-weight: 600; margin: 6px 0 4px; } .result-card .table { margin-bottom: 8px; }
    details.code-card { border: 1px solid #ebe5e5; border-radius: 8px; background: #fff; margin-bottom: 16px; }
    details.code-card > summary { cursor: pointer; padding: 9px 16px; color: #6e5c5d; font-weight: 500; font-size: 13.5px; user-select: none; }
    details.code-card > summary:hover { color: %1$s; }
    details.code-card[open] > summary { border-bottom: 1px solid #f4f0f0; }
    details.code-card .code-card-hint { color: #978a8b; font-weight: 400; font-size: 12.5px; margin-left: 6px; }
    .code-card-body { padding: 12px 16px; }
    .code-card-body .btn { background: #fdfcfc; border: 1px solid #e4dcdc; color: #5d4b4c; font-size: 12px; padding: 2px 10px; border-radius: 5px; }
    .code-card-body .btn:hover { background: #f5eded; color: %1$s; border-color: %1$s; }

    /* Academic / Journal Table Styling (Data-ink ratio / Tufte style) */
    .table { margin-bottom: 8px; }
    .table-striped > tbody > tr:nth-of-type(odd) > * { --bs-table-accent-bg: #fcfafa; }
    .table > thead th { background: transparent; border-top: 1.5px solid #222; border-bottom: 1.5px solid #222; color: #251819; font-weight: 600; font-size: 13.5px; padding: 8px 12px; white-space: nowrap; }
    .table > tbody td { border-bottom: 1px solid #f2eeee; padding: 7px 12px; font-size: 14px; color: #302223; }
    .table > tbody > tr:last-child > td { border-bottom: 1.5px solid #222; }

    /* Chulabhorn Likit has proportional digits and no tnum feature, so number cells use a system font with
       tabular figures; this keeps the decimal points of a column in line */
    .table td.num, .shiny-table td { font-family: 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                                     font-variant-numeric: tabular-nums; white-space: nowrap; }
    
    /* sidebar layouts: a sidebar of fixed width on wide screens leaves the rest to the tables, which scroll
       sideways inside their own box when still too wide; below 992px the sidebar goes above the results */
    @media (min-width: 992px) {
      .row > .col-sm-4:has(> .well) { flex: 0 0 clamp(280px, 26vw, 360px); width: auto; }
      .row > .col-sm-8[role=main] { flex: 1 1 0; width: auto; min-width: 0; } }
    @media (min-width: 576px) and (max-width: 991.98px) {
      .row > .col-sm-4:has(> .well), .row > .col-sm-8[role=main] { width: 100%%; } }
    .equation { font-family: monospace; font-size: 14px; background: #f8f4f4; border: 1px solid #ebe5e5; padding: 10px 14px; border-radius: 6px; }
    .empty-state { text-align: center; color: #667; background: #fff; border: 1px dashed #dfd6d6; border-radius: 10px; padding: 40px 24px; margin-bottom: 16px; }
    .shiny-html-output.stale-dim { display: block; }   /* uiOutput is display: contents, which opacity does not apply to */
    .is-stale .stale-dim { opacity: .35; filter: grayscale(1); user-select: none; transition: opacity .2s; }
    .flex-none { flex: none; white-space: nowrap; }
    /* Live View plot preview frame */
    .plot-live-wrapper { margin: 12px 0 20px; }
    .plot-live-card {
      max-width: 720px;
      margin: 0 auto;
      background: #ffffff;
      border: 1px solid #e4dcdc;
      border-radius: 12px;
      box-shadow: 0 4px 18px rgba(0, 0, 0, .05);
      padding: 14px 18px 18px;
      transition: box-shadow .2s;
    }
    .plot-live-card:hover { box-shadow: 0 6px 24px rgba(0, 0, 0, .08); }
    .plot-live-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-wrap: wrap;
      gap: 8px;
      margin-bottom: 12px;
      padding-bottom: 8px;
      border-bottom: 1px solid #f2eded;
    }
    .plot-live-title { font-weight: 600; font-size: 13.5px; color: #2e2021; display: inline-flex; align-items: center; }
    .plot-live-viewport {
      width: 100%%;
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 260px;
      background: #fdfdfd;
      border-radius: 6px;
      overflow: hidden;
    }
    .plot-live-viewport .shiny-plot-output {
      display: flex;
      justify-content: center;
      align-items: center;
      width: auto !important;
      max-width: 100%%;
      height: auto !important;
    }
    .plot-live-viewport img {
      max-width: 100%% !important;
      height: auto !important;
      display: block;
      border-radius: 4px;
    }
    .opt-disabled, .opt-disabled * { color: #9aa0a6 !important; cursor: not-allowed; }
    .light-dot { display: inline-block; width: 12px; height: 12px; border-radius: 50%%; margin-right: 6px; vertical-align: -1px; }
    .light-green { background: #9e2e44; } .light-yellow { background: #f0b400; } .light-red { background: #d33a2c; }
    .empty-state i { font-size: 32px; color: %1$s; margin-bottom: 10px; } .empty-state h5 { font-weight: 600; color: #333; }",
  brand[["main"]],
  brand[["dark"]],
  brand[["light"]]
)

