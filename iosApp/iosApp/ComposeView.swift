//
//  ComposeView.swift
//  iosApp
//
//  Created by David Cruz Anaya on 03/10/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct ComposeView: UIViewControllerRepresentable {
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        // no-op
    }
    
    func makeUIViewController(context: Context) -> some UIViewController {
        AppKt.MainViewController()
    }
}

