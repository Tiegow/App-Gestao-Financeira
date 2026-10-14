package br.ufrn.gerfin.app.data

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase =
    createSupabaseClient(
        supabaseUrl = "https://camrvlqvziwgutazhpqz.supabase.co",
        supabaseKey = "sb_publishable_h29grU9nQDVSRrPij9PBeQ_NcQ-bGAo",
    ) {
        install(Auth)
        install(Postgrest)
    }
